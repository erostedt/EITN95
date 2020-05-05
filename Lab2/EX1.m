%% Primal, maximize
c_p = -[13, 11]';
Aineq_p = [4, 5; 5, 3; 1, 2];
bineq_p = [1500, 1575, 420]';

lb_p = [0, 0]';

options = optimoptions('linprog', 'Algorithm', 'dual-simplex', 'Display', 'off');
[x_p,fval_p,exitflag_p,output_p,lambda_p] = linprog(c_p, Aineq_p, bineq_p, [], [], lb_p, [], [], options);

%% Dual, minimize

c_d = [1500, 1575, 420]';
Aineq_d = -[4, 5, 1; 5, 3, 2];
bineq_d = -[13, 11]';

lb_d = [0, 0, 0]';

options = optimoptions('linprog', 'Algorithm', 'dual-simplex', 'Display', 'off');
[x_d,fval_d,exitflag_d,output_d,lambda_d] = linprog(c_d, Aineq_d, bineq_d, [], [], lb_d, [], [], options);


%% Integer programming

c_i = ones(7,1);
A_ineq_i = - [1, 0, 0, 1, 1, 1, 1;
    1, 1, 0, 0, 1, 1, 1;
    1, 1, 1, 0, 0, 1, 1;
    1, 1, 1, 1, 0, 0, 1;
    1, 1, 1, 1, 1, 0, 0;
    0, 1, 1, 1, 1, 1, 0;
    0, 0, 1, 1, 1, 1, 1;
    ];

b_ineq_i = -[8, 6, 5, 4, 6, 7, 9]';
intcon = [1, 2, 3, 4, 5, 6, 7];
lb_i = [0,0,0,0,0,0,0];
options = optimoptions('intlinprog', 'Display', 'off');

[x_i, fval_i, exitflag_i, output_i] = intlinprog(c_i, intcon, A_ineq_i, b_ineq_i, [], [], lb_i, [], options);
[x_i_relaxed, fval_i_relaxed, exitflag_i_relaxed, output_i_relaxed] = intlinprog(c_i, [], A_ineq_i, b_ineq_i, [], [], lb_i, [], options);