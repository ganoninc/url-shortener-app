import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "./store";

type User = {
  email: string;
};

export type AuthenticatedAuthState = {
  status: "authenticated";
  jwt: string;
  user: User;
};

type UnauthenticatedAuthState = {
  status: "unauthenticated";
};

export type AuthState = UnauthenticatedAuthState | AuthenticatedAuthState;

const initialState = {
  status: "unauthenticated",
} as AuthState;

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (_state, action: PayloadAction<AuthenticatedAuthState>) => {
      return {
        status: "authenticated",
        jwt: action.payload.jwt,
        user: action.payload.user,
      };
    },
    logout: () => initialState,
  },
});

export const { setCredentials, logout } = authSlice.actions;
export const selectUser = (state: RootState) => {
  if (state.auth.status === "authenticated") {
    return state.auth.user;
  }

  return null;
};
export const selectJwt = (state: RootState) => {
  if (state.auth.status === "authenticated") {
    return state.auth.jwt;
  }

  return null;
};
export const selectStatus = (state: RootState) => {
  return state.auth.status;
};

export default authSlice.reducer;
