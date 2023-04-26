import React from "react";
import GroupList from "../../components/group/grouplist/GroupList";

import EnterButton from "../../components/group/groupnav/EnterButton";
import GroupNav from "../../components/group/groupnav/GroupNav";
import MakeButton from "../../components/group/groupnav/MakeButton";

export default function GroupPage() {
  return (
    <>
      <div>그룹페이지입니다.</div>
      <GroupNav />
      <MakeButton />
      <EnterButton />
      <GroupList />
    </>
  );
}
