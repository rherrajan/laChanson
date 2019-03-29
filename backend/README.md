laChanson
=========

Description
----------------------------------------------------
Mittelalterliche Simulation eines von Banditen bedrohten Dorfes


Build the dynamic backend
-------------------------

```bash
	cd backend
```
```bash
	mvn install; heroku local
```

view http://localhost:5000

Deploy the dynamic backend
-------------------------

create a github repository (without readme)
	https://github.com/new

push to the new repo
```bash
	git init
	git add -A
	git commit -m "initial commit"
	git remote add origin https://github.com/<your user name>/laChanson.git
    git push -u origin master
```

create heroku app
```bash
	cd backend
	heroku create laChanson
```

connect heroku app with github
	https://dashboard.heroku.com/apps/laChanson/deploy/github

enable automatic deploys

test manual deploy
	click "Deploy Branch" and watch the logs
	wait for "Your app was successfully deployed"

Open your deployed app
	https://laChanson.herokuapp.com/systeminfo
