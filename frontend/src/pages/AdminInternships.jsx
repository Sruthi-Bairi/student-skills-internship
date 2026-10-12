import { useEffect, useState } from "react";
import { api } from "../api/client.js";

const blank = {
  title: "",
  company: "",
  location: "",
  description: "",
  requiredSkills: "",
  duration: "",
  stipend: "",
  deadline: "",
  active: true,
};

export default function AdminInternships() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState(blank);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");

  async function load() {
    setItems(await api("/api/admin/internships"));
  }

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, []);

  function update(field) {
    return (event) => {
      const value = field === "active" ? event.target.checked : event.target.value;
      setForm({ ...form, [field]: value });
    };
  }

  async function onSubmit(event) {
    event.preventDefault();
    setError("");
    const payload = { ...form, deadline: form.deadline || null };
    try {
      if (editingId) {
        await api(`/api/admin/internships/${editingId}`, { method: "PUT", body: JSON.stringify(payload) });
      } else {
        await api("/api/admin/internships", { method: "POST", body: JSON.stringify(payload) });
      }
      setForm(blank);
      setEditingId(null);
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  async function remove(id) {
    setError("");
    try {
      await api(`/api/admin/internships/${id}`, { method: "DELETE" });
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="split">
      <form className="panel form" onSubmit={onSubmit}>
        <h1>{editingId ? "Edit internship" : "Add internship"}</h1>
        {error && <p className="error">{error}</p>}
        <label>
          Title
          <input required value={form.title} onChange={update("title")} />
        </label>
        <label>
          Company
          <input required value={form.company} onChange={update("company")} />
        </label>
        <label>
          Location
          <input value={form.location} onChange={update("location")} />
        </label>
        <label>
          Description
          <textarea required rows="4" value={form.description} onChange={update("description")} />
        </label>
        <label>
          Required skills
          <input value={form.requiredSkills} onChange={update("requiredSkills")} />
        </label>
        <label>
          Duration
          <input value={form.duration} onChange={update("duration")} />
        </label>
        <label>
          Stipend
          <input value={form.stipend} onChange={update("stipend")} />
        </label>
        <label>
          Deadline
          <input type="date" value={form.deadline || ""} onChange={update("deadline")} />
        </label>
        <label className="checkbox">
          <input type="checkbox" checked={form.active} onChange={update("active")} />
          Active / open for applications
        </label>
        <div className="actions">
          <button className="btn" type="submit">
            {editingId ? "Update" : "Create"}
          </button>
          {editingId && (
            <button
              className="btn btn-ghost"
              type="button"
              onClick={() => {
                setEditingId(null);
                setForm(blank);
              }}
            >
              Cancel
            </button>
          )}
        </div>
      </form>
      <section className="panel">
        <h2>Posted internships</h2>
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Company</th>
              <th>Open</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {items.map((item) => (
              <tr key={item.id}>
                <td>{item.title}</td>
                <td>{item.company}</td>
                <td>{item.active ? "Yes" : "No"}</td>
                <td className="row-actions">
                  <button
                    className="btn btn-small"
                    type="button"
                    onClick={() => {
                      setEditingId(item.id);
                      setForm({
                        title: item.title,
                        company: item.company,
                        location: item.location || "",
                        description: item.description,
                        requiredSkills: item.requiredSkills || "",
                        duration: item.duration || "",
                        stipend: item.stipend || "",
                        deadline: item.deadline || "",
                        active: item.active,
                      });
                    }}
                  >
                    Edit
                  </button>
                  <button className="btn btn-small btn-danger" type="button" onClick={() => remove(item.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
}
