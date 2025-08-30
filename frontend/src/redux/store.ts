import { combineReducers, configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";

const rootReducer = combineReducers({
  auth: authReducer,
});

export function setupStore(preloadedState?: Partial<RootState>) {
  return configureStore({
    reducer: rootReducer,
    preloadedState,
  });
}

export type RootState = ReturnType<typeof rootReducer>;
export type AppStore = ReturnType<typeof configureStore>;
export type AppDispatch = AppStore["dispatch"];
