import Nav from "@/components/Nav";

export default function navLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="w-screen h-screen">
      <Nav />
      <div className="h-[92%]">{children}</div>
    </div>
  );
}
