export default function StatusBadge({ status }) {
  const cls = String(status || "").toLowerCase();
  return <span className={`badge ${cls}`}>{status}</span>;
}
