import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  eventimageurl: "",
  eventtype: "",
};

export const event = createSlice({
  name: "event",
  initialState,
  reducers: {
    reset: () => initialState,
    eventimageurl: (state, action: PayloadAction<string>) => {
      state.eventimageurl = action.payload;
    },
    eventtype: (state, action: PayloadAction<string>) => {
      state.eventtype = action.payload;
    },
  },
});

export const { reset, eventimageurl, eventtype } = event.actions;
export default event.reducer;
