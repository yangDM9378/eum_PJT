import { configureStore } from "@reduxjs/toolkit";
import coordsReducer from "./map/slice";
import addEventReducer from "./addevent/addEventSlice";
export const store = configureStore({
  reducer: { coordsReducer, addEventReducer },
  devTools: process.env.NODE_ENV !== "production",
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
