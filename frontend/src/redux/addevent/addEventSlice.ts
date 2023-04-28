import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  originimageurl: "",
};

export const addevent = createSlice({
  name: "addevent",
  initialState,
  reducers: {
    reset: () => initialState,
    originimageurl: (state, action: PayloadAction<string>) => {
      state.originimageurl = action.payload;
    },
  },
});

export const { reset, originimageurl } = addevent.actions;
export default addevent.reducer;
