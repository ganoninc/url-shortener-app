import axios from "axios";
import { store, type RootState } from "../redux/store";
import type { Store } from "@reduxjs/toolkit";
import { UrlControllerApi } from "./url/generated";
import { apiGatewayUrl } from "../config/apiGateway";
import { refreshAccessToken } from "./auth/refreshAccessToken";
import {
  logoutDueToExpiredAccessToken,
  setCredentials,
} from "../redux/authSlice";

const createClient = (store: Store<RootState>) => {
  const client = axios.create({
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
  });

  client.interceptors.request.use((config) => {
    const authState = store.getState().auth;
    if (authState.status === "authenticated") {
      config.headers.Authorization = `Bearer ${authState.accessToken}`;
    }

    return config;
  });

  client.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;

      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;
        const { auth } = store.getState();

        if (auth.status === "unauthenticated") {
          store.dispatch(logoutDueToExpiredAccessToken());
          return Promise.reject(
            "Cannot refresh access token: user is not authenticated."
          );
        }

        try {
          const newAccessToken = await refreshAccessToken();
          store.dispatch(
            setCredentials({
              ...auth,
              accessToken: newAccessToken,
            })
          );

          error.config.headers.Authorization = `Bearer ${newAccessToken}`;

          return client(originalRequest);
        } catch (refreshTokenError) {
          store.dispatch(logoutDueToExpiredAccessToken());
          return Promise.reject(refreshTokenError);
        }
      } else {
        return Promise.reject(error);
      }
    }
  );

  return client;
};

const apiClient = createClient(store);

export const urlService = new UrlControllerApi(
  undefined,
  apiGatewayUrl + "/url",
  apiClient
);
