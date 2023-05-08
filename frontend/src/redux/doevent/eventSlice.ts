import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  eventimageurl: "",
  eventtype: "",
  pictureimg: "",
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
    pictureimg: (state, action: PayloadAction<string>) => {
      state.pictureimg = action.payload;
    },
  },
});

export const { reset, eventimageurl, eventtype, pictureimg } = event.actions;
export default event.reducer;
