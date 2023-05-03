import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
    originimageurl:'',
};

export const eventpose = createSlice({
    name:"event",
    initialState,
    reducers: {
        reset: () => initialState,
        poseimageurl: (state,action:PayloadAction<string>) => {
            state.originimageurl  = action.payload
        },
    }
});

export const {reset, poseimageurl} = eventpose.actions;
export default eventpose.reducer;