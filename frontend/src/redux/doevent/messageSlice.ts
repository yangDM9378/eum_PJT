import { createSlice, PayloadAction } from "@reduxjs/toolkit";


const initialState = {
    pictureid: 0,
};

export const picture = createSlice({
    name: 'picture',
    initialState,
    reducers : {
        reset: () => initialState,
        pictureid: (state,action: PayloadAction<number>) => {
            state.pictureid = action.payload;
        }
        
    }
});

export const {reset, pictureid} = picture.actions;
export default picture.reducer