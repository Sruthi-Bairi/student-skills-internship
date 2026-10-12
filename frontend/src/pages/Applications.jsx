import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBadge from "../components/StatusBadge.jsx";

export default function Applications() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    api("/api/applications/me")
      .then(setItems)
      .catch((err) => setError(err.message));
  }, []);

  return (
    <section className="panel">
      <h1>My applications</h1>
      {error && <p className="error">{error}</p>}
      {items.length === 0 ? (
        <p className="muted">You have not applied yet.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Internship</th>
              <th>Company</th>
              <th>Status</th>
              <th>Applied</th>
            </tr>
          </thead>
          <tbody>
            {items.map((item) => (
              <tr key={item.id}>
                <td>{item.internshipTitle}</td>
                <td>{item.company}</td>
                <td>
                  <StatusBadge status={item.status} />
                </td>
                <td>{new Date(item.appliedAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  );
}
