import { fireEvent, screen } from "@testing-library/react";
import { setupStore } from "../../redux/store";
import { renderWithProviders } from "../../utils/test-utils";
import NewShortUrlPage from "./NewShortUrlPage";
import { vi } from "vitest";
import { updateOriginalUrl } from "../../redux/newShortUrlSlice";

describe("NewShortUrlPage", () => {
  it("dispatches updateOriginalUrl action when original URL input has a new value", () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http",
      },
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
    });

    renderWithProviders(<NewShortUrlPage />, { store });
    const originalUrlTextInput = screen.getByPlaceholderText(
      "Enter your link here"
    );
    const submitButton = screen.getByRole("button");

    expect(originalUrlTextInput).toHaveClass("has-error");
    expect(submitButton).toBeDisabled();
  });

  it("enables submit button and gives visual feedback when originalUrl is valid", () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "http://localhost",
      },
    });

    renderWithProviders(<NewShortUrlPage />, { store });
    const originalUrlTextInput = screen.getByPlaceholderText(
      "Enter your link here"
    );
    const submitButton = screen.getByRole("button");

    expect(originalUrlTextInput).toHaveClass("is-valid");
    expect(submitButton).toBeEnabled();
  });

  it("disables submit button when originalUrl is empty", () => {
    const store = setupStore({
      newShortUrl: {
        originalUrl: "",
      },
    });

    renderWithProviders(<NewShortUrlPage />, { store });
    const submitButton = screen.getByRole("button");

    expect(submitButton).toBeDisabled();
  });
});
