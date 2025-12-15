document.addEventListener('DOMContentLoaded', () => {
  const MAX_POINTS = 1500;

  fetch('json/greenPoints.json')
    .then(response => response.json())
    .then(data => {
      let greenPointsTotal = 0;

      data.forEach(item => {
        greenPointsTotal += Number(item.points);
      });

      updateProgress(greenPointsTotal);
      updateNextReward(greenPointsTotal);
    });
});

function updateProgress(points) {
  const MAX_POINTS = 1500;
  const percent = Math.min((points / MAX_POINTS) * 100, 100);

  const fill = document.getElementById('points-progress');
  const pointsText = document.querySelector('.summary-value');


  fill.style.width = percent + '%';
  pointsText.textContent = points;
}

function updateNextReward(points) {
  const REWARD_1 = 500;
  const REWARD_2 = 1000;
  const REWARD_3 = 1500;

  const nextRewardText = document.querySelector(".summary-goal");
  const nextRewardInPercentages = document.querySelector(".summary-note");

  const cardPizza = document.getElementById("pizza-card");
  const cardOutdoorNight = document.getElementById("outdoor-card");
  const cardWellness = document.getElementById("wellness-card");

  const unlockedPizza = document.getElementById("pizza-unlocked");
  const unlockedOutdoor = document.getElementById("outdoor-unlocked");
  const unlockedWellness = document.getElementById("wellness-unlocked");


  let targetPoints = 0;
  let basePoints = 0;

  if (points < REWARD_1) {
    targetPoints = REWARD_1;
    basePoints = 0;
    nextRewardText.textContent =
      `Next reward: pizza party at ${REWARD_1} points.`;


    unlockedPizza.textContent = "Not yet unlocked";
    unlockedOutdoor.textContent = "Not yet unlocked";
    unlockedWellness.textContent = "Not yet unlocked";

  } else if (points < REWARD_2) {
    targetPoints = REWARD_2;
    basePoints = REWARD_1;
    nextRewardText.textContent =
      `Next reward: outdoor movie night at ${REWARD_2} points.`;
    cardPizza.classList.replace('reward-card-next', 'reward-card-done');

    unlockedPizza.textContent = "Already unlocked";
    unlockedOutdoor.textContent = "Not yet unlocked";
    unlockedWellness.textContent = "Not yet unlocked";
  } else if (points < REWARD_3) {
    targetPoints = REWARD_3;
    basePoints = REWARD_2;
    nextRewardText.textContent =
      `Next reward: wellness day at ${REWARD_3} points.`;
    cardPizza.classList.replace('reward-card-next', 'reward-card-done');
    cardOutdoorNight.classList.replace('reward-card-next', 'reward-card-done');

    unlockedPizza.textContent = "Already unlocked";
    unlockedOutdoor.textContent = "Already unlocked";
    unlockedWellness.textContent = "Not yet unlocked";
  } else {
    nextRewardText.textContent = "All rewards unlocked!";
    nextRewardInPercentages.textContent = "";
    cardPizza.classList.replace('reward-card-next', 'reward-card-done');
    cardOutdoorNight.classList.replace('reward-card-next', 'reward-card-done');
    cardWellness.classList.replace('reward-card-next', 'reward-card-done');

    unlockedPizza.textContent = "Already unlocked";
    unlockedOutdoor.textContent = "Already unlocked";
    unlockedWellness.textContent = "Already unlocked";

    return;
  }

  const percent = Math.min(
    Math.round(((points - basePoints) / (targetPoints - basePoints)) * 100), 100);
  nextRewardInPercentages.textContent =
    `${percent} percent of the way to the next reward.`;
}
