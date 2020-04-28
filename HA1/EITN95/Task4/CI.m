function [lb, ub] = CI(dps)
[N, ~] = size(dps);
mean_ = mean(dps);
var_ = var(dps);
ME = sqrt(var_/N);

lb = mean_ - 1.96*ME;
ub = mean_ + 1.96*ME;
end

