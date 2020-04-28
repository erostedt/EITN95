load N=100x=10lambda=4T=4M=4000
measurements = N_100x_10lambda_4T_4M_4000(:, 1);
nbrInQueue = N_100x_10lambda_4T_4M_4000(:, 2);

[lb, ub] = CI(nbrInQueue);
width = ub - lb;

% plot(measurements, nbrInQueue)