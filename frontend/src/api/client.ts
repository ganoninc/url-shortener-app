import axios from "axios";
import { store, type RootState } from "../redux/store";
import type { Store } from "@reduxjs/toolkit";
import { UrlControllerApi } from "./url/generated";
import { apiGatewayUrl } from "../config/apiGateway";

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
      config.headers.Authorization = `Bearer ${authState.jwt}`;
    }

    return config;
  });

  return client;
};

const apiClient = createClient(store);

export const urlService = new UrlControllerApi(
  undefined,
  apiGatewayUrl + "/url",
  apiClient
);
