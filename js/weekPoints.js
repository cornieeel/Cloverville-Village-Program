document.addEventListener('DOMContentLoaded', () => {

  // When the info icon is clicked, show/hide the info block by toggling a CSS class
  document.getElementById("info-icon").addEventListener("click", () => {
    document.getElementById("info-block").classList.toggle("is-collapsed");
  });

  fetch('./actualPoints.json')
    .then(response => response.json()) // Convert the HTTP response into JS data
    .then(data => {
      const today = new Date(); // Current date
      const weekAgo = new Date(today);
      weekAgo.setDate(today.getDate() - 7); // One week ago

      const tbody = document.getElementById("actions-table-body");
      let greenPointsLastWeek = 0; // Only points from the last 7 days

      data.forEach(item => {
        const pts = Number(item.pointsPerActivity);
        const d = new Date(item.date); // Convert item.date (string) into a real Date object

        // If the activity date is within the last 7 days, include it
        if (d >= weekAgo && d <= today) {
          greenPointsLastWeek += pts;

          // Create a new table row and fill it with the data
          const tr = document.createElement("tr");
          tr.innerHTML = `
              <td>${item.residentId}</td>
              <td>${item.activity}</td>
              <td>${pts}</td>
          `;
          tbody.appendChild(tr);
        }
      });

      // Show the total points earned in the last 7 days in the UI
      const lastWeekInfo = document.querySelector(".actions-points");
      lastWeekInfo.textContent = greenPointsLastWeek + ' points';

      const weekNumberElem = document.querySelector('.actions-title');
      const weekRangeElem = document.querySelector(".actions-goal");

      // Only update if those elements exist (prevents errors if the page layout changes)
      if (weekNumberElem && weekRangeElem) {
        const weekNumber = getWeekNumber(today); // Calculate the week number of the year
        weekNumberElem.textContent = 'Week ' + weekNumber;

        weekRangeElem.textContent = `${formatDate(weekAgo)} to ${formatDate(today)}`;
      }
    });
});

function getWeekNumber(date) {
  const firstJan = new Date(date.getFullYear(), 0, 1); // January 1st of the current year
  const diff = date - firstJan; // Difference in milliseconds between date and Jan 1
  const oneDay = 1000 * 60 * 60 * 24; // How many ms are in 1 day
  const dayNumber = Math.floor(diff / oneDay) + 1; // Day of the year (1..365/366)
  return Math.ceil(dayNumber / 7); // Rough week number (week 1 = days 1–7, etc.)
}

function formatDate(date) {
  const day = String(date.getDate()).padStart(2, '0'); // Always 2 digits (e.g. 03)
  const month = String(date.getMonth() + 1).padStart(2, '0'); // getMonth() is 0-based, so +1
  return `${day}.${month}`; // Return in DD.MM format
}
