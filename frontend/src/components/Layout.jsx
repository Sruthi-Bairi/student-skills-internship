import { Outlet } from "react-router-dom";
import NavBar from "./NavBar.jsx";

export default function Layout() {
  return (
    <div className="app-shell">
      <NavBar />
      <main className="content">
        <Outlet />
      </main>
      <footer className="footer">Student Skill and Internship Management System</footer>
    </div>
  );
}
