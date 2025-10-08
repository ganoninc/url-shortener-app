import { http, HttpResponse } from "msw";
import { server } from "../mocks/node";
import {
  logoutDueToExpiredAccessToken,
  setCredentials,
} from "../redux/authSlice";
import { store } from "../redux/store";
import { urlService } from "./client";
import { apiGatewayUrl } from "../config/apiGateway";
import { waitFor } from "@testing-library/react";
import { vi } from "vitest";
import { fakeAuthenticatedAuthState } from "../redux/fakes";

const shortId = "shortId";

describe("api client", () => {
  beforeAll(() => {
    server.listen();

    vi.spyOn(store, "dispatch");
    vi.spyOn(console, "error").mockImplementation(() => {});
  });

  beforeEach(() => {
    store.dispatch(setCredentials(fakeAuthenticatedAuthState));

    server.use(
      http.get(
        `${apiGatewayUrl}/url/my-urls/${shortId}`,
        () => {
          return HttpResponse.json(
            { error: "Expired access token" },
            { status: 401 }
          );
        },
        { once: true }
      )
    );
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  it("refreshes the access token", async () => {
    await urlService.getUserUrl(shortId);

    waitFor(() => {
      expect(store.dispatch).toHaveBeenCalledWith(
        setCredentials({
          ...fakeAuthenticatedAuthState,
          accessToken: "new-access-token",
        })
      );
    });
  });

  it("dispatches logoutDueToExpiredAccessToken when the refresh token is rejected by the auth-service", async () => {
    server.use(
      http.get(`${apiGatewayUrl}/auth/refresh-access-token`, () => {
        return HttpResponse.json({ error: "Bad Request" }, { status: 400 });
      })
    );

    await expect(urlService.getUserUrl(shortId)).rejects.toThrow();

    await waitFor(() => {
      expect(store.dispatch).toHaveBeenCalledWith(
        logoutDueToExpiredAccessToken()
      );
    });
  });
});
