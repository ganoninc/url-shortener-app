import { screen, fireEvent } from "@testing-library/react";
import { vi } from "vitest";
import { setCredentials } from "../../redux/authSlice";
import LoginPage from "./LoginPage";
import { renderWithProviders } from "../../utils/test-utils";
import { setupStore } from "../../redux/store";

vi.stubEnv("VITE_API_GATEWAY_URL", "http://localhost");

const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL;

const jwtToken = "fake-jwt-token";
const userEmail = "user@test.com";

describe("LoginPage", () => {
  beforeAll(() => {
    console.log(window);
    Object.defineProperty(window, "open", {
      writable: true,
      value: vi.fn(),
    });

    vi.spyOn(window, "addEventListener");
    vi.spyOn(window, "removeEventListener");
  });

  afterEach(() => {
    vi.resetAllMocks();
  });

  it("matches the login page snapshot", () => {
    const { container } = renderWithProviders(<LoginPage />);
    expect(container).toMatchSnapshot();
  });

  it("opens a popup and adds message listener when login with Google", () => {
    renderWithProviders(<LoginPage />);

    fireEvent.click(screen.getByRole("button", { name: /login with google/i }));

    expect(window.open).toHaveBeenCalledTimes(1);
    expect(window.open).toHaveBeenCalledWith(
      API_GATEWAY_URL + "/auth/oauth2/authorization/google",
      "viteUrlShortenerOauthLogin",
      "width=500,height=600"
    );

    expect(window.addEventListener).toHaveBeenCalledTimes(1);
    expect(window.addEventListener).toHaveBeenCalledWith(
      "message",
      expect.any(Function)
    );
  });

  it("handles oauth callback by dispatching setCredentials", () => {
    const store = setupStore();
    vi.spyOn(store, "dispatch");
    renderWithProviders(<LoginPage />, { store });

    fireEvent.click(screen.getByRole("button", { name: /login with google/i }));
    window.dispatchEvent(
      new MessageEvent("message", {
        data: { token: jwtToken, email: userEmail },
        origin: API_GATEWAY_URL,
      })
    );

    expect(store.dispatch).toHaveBeenCalledTimes(1);
    expect(store.dispatch).toHaveBeenCalledWith(
      setCredentials({
        status: "authenticated",
        jwt: jwtToken,
        user: { email: userEmail },
      })
    );
  });

  it("closes popup and removes message listener after handling message", () => {
    const popupMock = { close: vi.fn() };
    window.open = vi.fn().mockReturnValue(popupMock);

    renderWithProviders(<LoginPage />);

    fireEvent.click(screen.getByRole("button", { name: /login with google/i }));
    const messageEvent = new MessageEvent("message", {
      data: { token: jwtToken, email: userEmail },
      origin: API_GATEWAY_URL,
    });
    window.dispatchEvent(messageEvent);

    expect(popupMock.close).toHaveBeenCalledTimes(1);
    expect(window.removeEventListener).toHaveBeenCalledTimes(1);
    expect(window.removeEventListener).toHaveBeenCalledWith(
      "message",
      expect.any(Function)
    );
  });
});
