import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  originimageurl: "",
  agingselecturl: "",
};

export const addevent = createSlice({
  name: "addevent",
  initialState,
  reducers: {
    reset: () => initialState,
    originimageurl: (state, action: PayloadAction<string>) => {
      state.originimageurl = action.payload;
    },
    agingselecturl: (state, action: PayloadAction<string>) => {
      state.agingselecturl = action.payload;
    },
  },
});

export const { reset, originimageurl, agingselecturl } = addevent.actions;
export default addevent.reducer;
