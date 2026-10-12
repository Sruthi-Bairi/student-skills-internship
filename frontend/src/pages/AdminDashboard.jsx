import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/client.js";

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api("/api/admin/stats")
      .then(setStats)
      .catch((err) => setError(err.message));
  }, []);

  return (
    <div className="stack">
      <h1>Admin dashboard</h1>
      {error && <p className="error">{error}</p>}
      <div className="stats">
        <article className="stat">
          <span>Students</span>
          <strong>{stats?.students ?? "-"}</strong>
        </article>
        <article className="stat">
          <span>Internships</span>
          <strong>{stats?.internships ?? "-"}</strong>
        </article>
        <article className="stat">
          <span>Applications</span>
          <strong>{stats?.applications ?? "-"}</strong>
        </article>
        <article className="stat">
          <span>Pending</span>
          <strong>{stats?.pending ?? "-"}</strong>
        </article>
        <article className="stat">
          <span>Accepted</span>
          <strong>{stats?.accepted ?? "-"}</strong>
        </article>
        <article className="stat">
          <span>Rejected</span>
          <strong>{stats?.rejected ?? "-"}</strong>
        </article>
      </div>
      <div className="actions">
        <Link className="btn" to="/admin/internships">
          Manage internships
        </Link>
        <Link className="btn btn-ghost" to="/admin/applications">
          Review applications
        </Link>
        <Link className="btn btn-ghost" to="/admin/students">
          View students
        </Link>
      </div>
    </div>
  );
}
