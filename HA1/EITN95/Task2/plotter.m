load subTask1
times1 = subTask1(:, 1);
totalNbrInQueue1 = subTask1(:, 4);

load subTask2
times2 = subTask2(:, 1);
totalNbrInQueue2 = subTask2(:, 4);

load subTask3
times3 = subTask3(:, 1);
totalNbrInQueue3 = subTask3(:, 4);

%%
subplot(3, 1, 1)
plot(times1, totalNbrInQueue1)
xlabel('Time')
title('Sub task 1 (d=1)')

subplot(3, 1, 2)
plot(times2, totalNbrInQueue2, 'r')
xlabel('Time')
ylabel('Total number in queuing system')
title('Sub task 2 (d is exponentially distributed)')

subplot(3, 1, 3)
plot(times3, totalNbrInQueue3, 'g')
xlabel('Time')
title('Sub task 3 (Change of priority)')
