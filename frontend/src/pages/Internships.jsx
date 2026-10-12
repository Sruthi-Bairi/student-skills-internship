import { useEffect, useState } from "react";
import { api } from "../api/client.js";

export default function Internships() {
  const [items, setItems] = useState([]);
  const [query, setQuery] = useState({ q: "", location: "" });
  const [note, setNote] = useState({});
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function load(event) {
    event?.preventDefault();
    setError("");
    const params = new URLSearchParams();
    if (query.q) params.set("q", query.q);
    if (query.location) params.set("location", query.location);
    params.set("activeOnly", "true");
    const data = await api(`/api/internships?${params.toString()}`);
    setItems(data);
  }

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, []);

  async function apply(internshipId) {
    setError("");
    setMessage("");
    try {
      await api("/api/applications", {
        method: "POST",
        body: JSON.stringify({ internshipId, coverNote: note[internshipId] || "" }),
      });
      setMessage("Application submitted.");
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="stack">
      <h1>Internship opportunities</h1>
      <form className="filters panel" onSubmit={load}>
        <input
          placeholder="Search title, company, or skills"
          value={query.q}
          onChange={(e) => setQuery({ ...query, q: e.target.value })}
        />
        <input
          placeholder="Filter by location"
          value={query.location}
          onChange={(e) => setQuery({ ...query, location: e.target.value })}
        />
        <button className="btn" type="submit">
          Search
        </button>
      </form>
      {error && <p className="error">{error}</p>}
      {message && <p className="success">{message}</p>}
      <div className="cards">
        {items.map((item) => (
          <article className="panel card" key={item.id}>
            <h2>{item.title}</h2>
            <p className="muted">
              {item.company} · {item.location || "Location TBA"}
            </p>
            <p>{item.description}</p>
            <p>
              <strong>Skills:</strong> {item.requiredSkills || "Not specified"}
            </p>
            <p>
              {item.duration || "Duration flexible"} · {item.stipend || "Stipend not listed"}
            </p>
            <p className="muted">Deadline: {item.deadline || "Open"}</p>
            <textarea
              rows="2"
              placeholder="Optional cover note"
              value={note[item.id] || ""}
              onChange={(e) => setNote({ ...note, [item.id]: e.target.value })}
            />
            <button className="btn" type="button" onClick={() => apply(item.id)}>
              Apply
            </button>
          </article>
        ))}
      </div>
      {items.length === 0 && <p className="muted">No internships match these filters.</p>}
    </div>
  );
}
