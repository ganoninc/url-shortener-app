import axios from "axios";
import { type RootState } from "../redux/store";
import type { Store } from "@reduxjs/toolkit";

const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL;

export const createClient = (store: Store<RootState>) => {
  const client = axios.create({
    baseURL: API_GATEWAY_URL,
  });

  client.interceptors.request.use((config) => {
    const jwt = store.getState().auth.jwt;
    if (jwt) {
      config.headers.Authorization = `Bearer ${jwt}`;
    }

    return config;
  });
};
