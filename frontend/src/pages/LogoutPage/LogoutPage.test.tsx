import { describe, vi } from "vitest";
import { setupStore, type AppStore } from "../../redux/store";
import { logout, setCredentials } from "../../redux/authSlice";
import { renderWithProviders } from "../../utils/test-utils";
import LogoutPage from "./LogoutPage";

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe("LogoutPage", () => {
  let store: AppStore;

  beforeEach(() => {
    store = setupStore();
    store.dispatch(
      setCredentials({
        jwt: "naknsandoasndanso",
        user: { email: "sadsadadas" },
      })
    );
    vi.spyOn(store, "dispatch");
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it("dispatches a logout action on mount", () => {
    const store = setupStore();
    store.dispatch(
      setCredentials({
        jwt: "naknsandoasndanso",
        user: { email: "sadsadadas" },
      })
    );
    vi.spyOn(store, "dispatch");

    renderWithProviders(<LogoutPage />, { store });

    expect(store.dispatch).toHaveBeenCalledTimes(1);
    expect(store.dispatch).toHaveBeenCalledWith(logout());
  });

  it("redirects to / on mount", () => {
    renderWithProviders(<LogoutPage />, { store });
    expect(mockNavigate).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });
});
