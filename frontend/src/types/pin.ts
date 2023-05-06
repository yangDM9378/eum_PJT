export type Pin = {
  pinId: number;
  latitude: number;
  longitude: number;
};

export type PinData = {
  result: [];
  resultCode: string;
  resultMsg: string;
};

export type Pindetail = {
  title: string;
  content: string;
  image: string;
  createdDate: string;
  userName: string;
  type: string;
};

export type PindetailResult = {
  result: Pindetail;
  resultCode: string;
  resultMsg: string;
};
