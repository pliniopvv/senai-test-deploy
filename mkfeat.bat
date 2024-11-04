git switch -c %1
git add .
git commit -m "%2"
git checkout develop
git merge %1
git push
