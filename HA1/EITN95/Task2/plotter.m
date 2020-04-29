load subTask3
times = subTask3(:, 1);
nbrOfAInQueue = subTask3(:, 2);
nbrOfBInQueue = subTask3(:, 3);
totalNbrInQueue = subTask3(:, 4);

%%
subplot(3, 1, 1)
plot(times, nbrOfAInQueue)

subplot(3, 1, 2)
plot(times, nbrOfBInQueue, 'r')

subplot(3, 1, 3)
plot(times, totalNbrInQueue, 'g')