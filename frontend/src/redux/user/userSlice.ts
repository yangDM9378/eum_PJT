import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  name: "",
};

export const user = createSlice({
  name: "user",
  initialState,
  reducers: {
    reset: () => initialState,
    assignName: (state, action: PayloadAction<string>) => {
      state.name = action.payload;
    },
  },
});

export const { reset, assignName } = user.actions;
export default user.reducer;
