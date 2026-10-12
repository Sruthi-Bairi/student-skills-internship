import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";

export default function NavBar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  async function handleLogout() {
    await logout();
    navigate("/");
  }

  return (
    <header className="topbar">
      <NavLink to="/" className="brand">
        SSIMS
      </NavLink>
      <nav className="nav-links">
        {!user && (
          <>
            <NavLink to="/login">Login</NavLink>
            <NavLink to="/register" className="btn btn-small">
              Register
            </NavLink>
          </>
        )}
        {user?.role === "STUDENT" && (
          <>
            <NavLink to="/dashboard">Dashboard</NavLink>
            <NavLink to="/profile">Profile</NavLink>
            <NavLink to="/skills">Skills</NavLink>
            <NavLink to="/internships">Internships</NavLink>
            <NavLink to="/applications">Applications</NavLink>
          </>
        )}
        {user?.role === "ADMIN" && (
          <>
            <NavLink to="/admin">Admin</NavLink>
            <NavLink to="/admin/students">Students</NavLink>
            <NavLink to="/admin/internships">Internships</NavLink>
            <NavLink to="/admin/applications">Applications</NavLink>
          </>
        )}
      </nav>
      {user && (
        <div className="session">
          <span>{user.fullName || user.email}</span>
          <button className="btn btn-ghost btn-small" onClick={handleLogout}>
            Logout
          </button>
        </div>
      )}
    </header>
  );
}
