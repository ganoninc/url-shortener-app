import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "./store";

type User = {
  email: string;
};

export type AuthState =
  | {
      jwt: null;
      user: null;
    }
  | {
      jwt: string;
      user: User;
    };

const initialState: AuthState = {
  jwt: null,
  user: null,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (state: AuthState, action: PayloadAction<AuthState>) => {
      state.jwt = action.payload.jwt;
      state.user = action.payload.user;
    },
    logout: (state: AuthState) => {
      state.jwt = null;
      state.user = null;
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export const selectUser = (state: RootState) => state.auth.user;
export const selectJwt = (state: RootState) => state.auth.jwt;
export default authSlice.reducer;
