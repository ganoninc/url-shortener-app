import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "@reduxjs/toolkit/query";

export type NewShortUrlState = {
  originalUrl: string;
};

const initialState: NewShortUrlState = {
  originalUrl: "",
};

const newShortUrlSlice = createSlice({
  name: "newShortUrl",
  initialState,
  reducers: {
    updateOriginalUrl: (
      state: NewShortUrlState,
      action: PayloadAction<NewShortUrlState>
    ) => {
      state.originalUrl = action.payload.originalUrl;
    },
  },
});

export const { updateOriginalUrl } = newShortUrlSlice.actions;
export const selectOriginalUrl = (state: RootState) =>
  state.newShortUrl.originalUrl;
export default newShortUrlSlice.reducer;
