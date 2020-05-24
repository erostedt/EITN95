// Matlab code for plotting histogram.
mat = zeros(100, 190);
num_students = 20;

for i = 1:100
    copy_num = 19;
    curr_idx = 1;
    temp = [];
    while copy_num > 0
        for j = 1:copy_num
            temp = [temp, random(i, curr_idx + j)];
        end
        curr_idx = curr_idx + num_students + 1;
        copy_num = copy_num - 1;
    end
    mat(i, :) = temp;
end
 
all_vals = zeros(1, 190*100);
iter = 0;
for i = 1:100
    all_vals(iter*190 + 1: (iter+1) * 190) = mat(i, :);
    iter = iter + 1;
end

all_vals = all_vals./60;

histogram(all_vals);
xlim([0, 40])
xlabel('Minutes', 'FontSize', 18);
ylabel('Frequency', 'FontSize', 18);
title('V = U(1, 7) m/s', 'FontSize', 18);


