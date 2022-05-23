git branch --merged| egrep -v "(^\*|master|main|develop)" | xargs git branch -d
