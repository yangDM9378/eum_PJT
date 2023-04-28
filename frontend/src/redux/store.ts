import { configureStore } from "@reduxjs/toolkit";
import coordsReducer from "./map/slice";
export const store = configureStore({
  reducer: { coordsReducer },
  devTools: process.env.NODE_ENV === "development",
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
