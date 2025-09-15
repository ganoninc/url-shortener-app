import axios from "axios";
import { store, type RootState } from "../redux/store";
import type { Store } from "@reduxjs/toolkit";
import { UrlControllerApi } from "./url/generated";

const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL;

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
  API_GATEWAY_URL + "/url",
  apiClient
);
