document.addEventListener('DOMContentLoaded', () => {

    document.getElementById("info-icon").addEventListener("click", () => {
        document.getElementById("info-block").classList.toggle("is-collapsed");
    });

    fetch('json/greenPoints.json')
        .then(response => response.json())
        .then(data => {
            const today = new Date();
            const weekAgo = new Date(today);
            weekAgo.setDate(today.getDate() - 7);

            const tbody = document.getElementById("actions-table-body");
            let greenPointsLastWeek = 0;

            data.forEach(item => {
                const pts = Number(item.points);
                const d = new Date(item.dateOfActivity);

                if (d >= weekAgo && d <= today) {
                    greenPointsLastWeek += pts;


                    const tr = document.createElement("tr");
                    tr.innerHTML = `
              <td>${item.citizenId}</td>
              <td>${item.activity}</td>
              <td>${pts}</td>
                 `;
                    tbody.appendChild(tr);

                }
            });

            const lastWeekInfo = document.querySelector(".actions-points");
            lastWeekInfo.textContent = greenPointsLastWeek + ' points';



            const weekNumberElem = document.querySelector('.actions-title');
            const weekRangeElem = document.querySelector(".actions-goal");

            if (weekNumberElem && weekRangeElem) {
                const weekNumber = getWeekNumber(today);
                weekNumberElem.textContent = 'Week ' + weekNumber;

                weekRangeElem.textContent =
                    `${formatDate(weekAgo)} to ${formatDate(today)}`;
            }
        });
});

function getWeekNumber(date) {
    const firstJan = new Date(date.getFullYear(), 0, 1);
    const diff = date - firstJan;
    const oneDay = 1000 * 60 * 60 * 24;
    const dayNumber = Math.floor(diff / oneDay) + 1;
    return Math.ceil(dayNumber / 7);
}

function formatDate(date) {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    return `${day}.${month}`;
}
