import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBadge from "../components/StatusBadge.jsx";

export default function AdminApplications() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");

  async function load() {
    setItems(await api("/api/admin/applications"));
  }

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, []);

  async function updateStatus(id, status) {
    setError("");
    try {
      await api(`/api/admin/applications/${id}/status`, {
        method: "PATCH",
        body: JSON.stringify({ status }),
      });
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <section className="panel">
      <h1>Applications</h1>
      {error && <p className="error">{error}</p>}
      <table>
        <thead>
          <tr>
            <th>Student</th>
            <th>Internship</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.id}>
              <td>
                {item.studentName || item.studentEmail}
                <div className="muted">{item.studentEmail}</div>
              </td>
              <td>
                {item.internshipTitle}
                <div className="muted">{item.company}</div>
              </td>
              <td>
                <StatusBadge status={item.status} />
              </td>
              <td className="row-actions">
                <button className="btn btn-small" type="button" onClick={() => updateStatus(item.id, "ACCEPTED")}>
                  Accept
                </button>
                <button className="btn btn-small btn-danger" type="button" onClick={() => updateStatus(item.id, "REJECTED")}>
                  Reject
                </button>
                <button className="btn btn-small btn-ghost" type="button" onClick={() => updateStatus(item.id, "PENDING")}>
                  Pending
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
