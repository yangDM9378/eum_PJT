import { configureStore } from "@reduxjs/toolkit";
import coordsReducer from "./map/mapSlice";
import addEventReducer from "./addevent/addEventSlice";
import poseEventReducer from "./doevent/DoEventSlice";

export const store = configureStore({
  reducer: { coordsReducer, addEventReducer, poseEventReducer },
  devTools: process.env.NODE_ENV !== "production",
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
