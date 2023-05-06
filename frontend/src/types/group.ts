export type Group = {
  groupId: number;
  name: string;
  image: string;
  description: string;
};

export type GroupDetail = {
  groupId: number;
  name: string;
  created_date: Date;
  image: string;
  description: string;
  groupCode: string;
};

export type GroupResult = {
  groupId: number;
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
