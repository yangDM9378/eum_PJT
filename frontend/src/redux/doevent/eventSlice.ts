import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  eventimageurl: "",
};

export const event = createSlice({
  name: "event",
  initialState,
  reducers: {
    reset: () => initialState,
    eventimageurl: (state, action: PayloadAction<string>) => {
      state.eventimageurl = action.payload;
    },
  },
});

export const { reset, eventimageurl } = event.actions;
export default event.reducer;
