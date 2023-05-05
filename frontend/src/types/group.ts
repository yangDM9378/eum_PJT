export type Group = {
  id: number;
  name: string;
  image: File;
  description: string;
};

export type GroupDetail = {
  group_id: number;
  name: string;
  created_date: Date;
  image: File;
  description: string;
  code: string;
};

export type GroupResult = {
  group_id: number;
  name: string;
  image: File;
  description: string;
};

export type GroupCode = {
  code: string;
};
export type GroupCodeResult = {
  result: null;
  resultCode: string;
  resultMsg: string;
};
