import { fireEvent, screen, waitFor } from "@testing-library/react";
import { setupStore } from "../../redux/store";
import { renderWithProviders } from "../../utils/test-utils";
import NewShortUrlPage from "./NewShortUrlPage";
import { vi } from "vitest";
import { updateOriginalUrl } from "../../redux/newShortUrlSlice";
import TextInputStyles from "../../components/TextInput/TextInput.module.css";

import { afterEach } from "vitest";
import { server } from "../../mocks/node";
import { ROUTES } from "../../routePaths";
import { fakeShortenURLResponse } from "../../mocks/fakes";
import { fakeAuthenticatedAuthState } from "../../redux/fakes";
import { urlService } from "../../api/client";
import { http, HttpResponse } from "msw";
import { apiGatewayUrl } from "../../config/apiGateway";

const mockedUseNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
  const actual = vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockedUseNavigate,
  };
});

window.alert = vi.fn();

describe("NewShortUrlPage", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  it("dispatches updateOriginalUrl action when original URL input has a new value", () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http",
      },
      auth: fakeAuthenticatedAuthState,
    });
    vi.spyOn(store, "dispatch");
    const updatedOriginalUrl = "http:";

    renderWithProviders(<NewShortUrlPage />, { store });
    const originalUrlTextInput = screen.getByPlaceholderText(
      "Enter your link here"
    );
    fireEvent.change(originalUrlTextInput, {
      target: { value: updatedOriginalUrl },
    });

    expect(store.dispatch).toHaveBeenCalledTimes(1);
    expect(store.dispatch).toHaveBeenCalledWith(
      updateOriginalUrl({ originalUrl: updatedOriginalUrl })
    );
  });

  it("disables submit button and gives visual feedback when originalUrl is invalid", () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "a-wrong-url",
      },
      auth: fakeAuthenticatedAuthState,
    });
    renderWithProviders(<NewShortUrlPage />, { store });

    const originalUrlTextInput = screen.getByPlaceholderText(
      "Enter your link here"
    );
    const submitButton = screen.getByRole("button");

    expect(originalUrlTextInput).toHaveClass(TextInputStyles.hasError);
    expect(submitButton).toBeDisabled();
  });

  it("enables submit button and gives visual feedback when originalUrl is valid", () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http://localhost",
      },
      auth: fakeAuthenticatedAuthState,
    });
    renderWithProviders(<NewShortUrlPage />, { store });

    const originalUrlTextInput = screen.getByPlaceholderText(
      "Enter your link here"
    );
    const submitButton = screen.getByRole("button");

    expect(originalUrlTextInput).toHaveClass(TextInputStyles.isValid);
    expect(submitButton).toBeEnabled();
  });

  it("disables submit button and gives no visual feedback when originalUrl is empty", () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "",
      },
      auth: fakeAuthenticatedAuthState,
    });
    renderWithProviders(<NewShortUrlPage />, { store });

    const originalUrlTextInput = screen.getByPlaceholderText(
      "Enter your link here"
    );
    const submitButton = screen.getByRole("button");

    expect(originalUrlTextInput).not.toHaveClass();
    expect(submitButton).toBeDisabled();
  });

  it("call urlService.shortenUrl() when handling url submit.", async () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http://test.com",
      },
      auth: fakeAuthenticatedAuthState,
    });

    const shortenUrlSpy = vi.spyOn(urlService, "shortenUrl");

    renderWithProviders(<NewShortUrlPage />, { store });

    const submitButton = screen.getByRole("button");
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(shortenUrlSpy).toHaveBeenCalledTimes(1);
      expect(shortenUrlSpy).toHaveBeenCalledWith({
        originalUrl: "http://test.com",
      });
    });
  });

  it("redirects to ROUTES.login when handling url submit without beeing logged in.", async () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http://test.com",
      },
    });

    renderWithProviders(<NewShortUrlPage />, { store });

    const submitButton = screen.getByRole("button");
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockedUseNavigate).toHaveBeenCalledTimes(1);
      expect(mockedUseNavigate).toHaveBeenCalledWith(ROUTES.login);
    });
  });

  it("resets the store when it gets a successful answer from the url-service and redirects to ROUTES.userShortUrlDetails()", async () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http://localhost",
      },
      auth: fakeAuthenticatedAuthState,
    });
    vi.spyOn(store, "dispatch");
    renderWithProviders(<NewShortUrlPage />, { store });

    const submitButton = screen.getByRole("button");
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(store.dispatch).toHaveBeenCalledTimes(1);
      expect(store.dispatch).toHaveBeenCalledWith(
        updateOriginalUrl({ originalUrl: "" })
      );
      expect(mockedUseNavigate).toHaveBeenCalledTimes(1);
      expect(mockedUseNavigate).toHaveBeenCalledWith(
        ROUTES.userNewShortUrlCreated(fakeShortenURLResponse.shortId)
      );
    });
  });

  it("displays an error when it gets an error from url-service", async () => {
    server.use(
      http.post(
        `${apiGatewayUrl}/url/shorten`,
        () => {
          return new HttpResponse(null, { status: 500 });
        },
        { once: true }
      )
    );
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http://localhost",
      },
      auth: fakeAuthenticatedAuthState,
    });
    vi.spyOn(store, "dispatch");
    renderWithProviders(<NewShortUrlPage />, { store });

    expect(screen.queryByRole("alert")).not.toBeInTheDocument();

    const submitButton = screen.getByRole("button");
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errorMessage = screen.getByRole("alert");
      expect(errorMessage).toBeInTheDocument();
      expect(errorMessage).toHaveTextContent(
        "Request failed with status code 500"
      );

      expect(mockedUseNavigate).toHaveBeenCalledTimes(0);
      expect(store.dispatch).toHaveBeenCalledTimes(0);
    });
  });
});
