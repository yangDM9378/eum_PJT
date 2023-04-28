import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  originimagepth: "",
};

export const addevent = createSlice({
  name: "addevent",
  initialState,
  reducers: {
    reset: () => initialState,
    assign: (state, action: PayloadAction<string>) => {
      state.originimagepth = action.payload;
    },
  },
});

export const { reset, assign } = addevent.actions;
export default addevent.reducer;
