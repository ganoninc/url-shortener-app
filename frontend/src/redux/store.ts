import { combineReducers, configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import newShortUrlRecuder from "./newShortUrlSlice";

const rootReducer = combineReducers({
  auth: authReducer,
  newShortUrl: newShortUrlRecuder,
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

export const store = setupStore();
