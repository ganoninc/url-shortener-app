import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "./store";

type User = {
  email: string;
};

export type AuthenticatedAuthState = {
  status: "authenticated";
  accessToken: string;
  user: User;
};

type UnauthenticatedAuthState = {
  status: "unauthenticated";
  reason?: "expiredRefreshToken" | "userLogout";
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
        accessToken: action.payload.accessToken,
        user: action.payload.user,
      };
    },
    logoutDueToUserAction: () =>
      ({
        ...initialState,
        reason: "userLogout",
      } as UnauthenticatedAuthState),
    logoutDueToExpiredAccessToken: () =>
      ({
        ...initialState,
        reason: "expiredRefreshToken",
      } as UnauthenticatedAuthState),
  },
});

export const {
  setCredentials,
  logoutDueToUserAction,
  logoutDueToExpiredAccessToken,
} = authSlice.actions;
export const selectUser = (state: RootState) => {
  if (state.auth.status === "authenticated") {
    return state.auth.user;
  }

  return null;
};
export const selectJwt = (state: RootState) => {
  if (state.auth.status === "authenticated") {
    return state.auth.accessToken;
  }

  return null;
};
export const selectStatus = (state: RootState) => {
  return state.auth.status;
};

export default authSlice.reducer;
