export type Pin = {
  pin_id: number;
  latitude: number;
  longitude: number;
};

export type PinData = {
  result: [];
  resultCode: string;
  resultMsg: string;
};

export type Pindetail = {
  pinId: Number;
  title: String;
  content: String;
  image: String;
  createdDate: Date;
  userName: String;
  type: String;
};

export type PindetailResult = {
  result: Pindetail;
  resultCode: string;
  resultMsg: string;
};
