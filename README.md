# DataCite Content Resolver

[![Build Status](https://travis-ci.org/datacite/content-resolver.svg)](https://travis-ci.org/datacite/content-resolver)

This software has been replaced by https://github.com/crosscite/content-negotiation in May 2017.

## Installation

Using Docker.

```
docker run -p 8080:8080 --env-file .env datacite/content-resolver
```

You can now point your browser to `http://localhost:8080` and use the application.

For a more detailed configuration, including using a lcoal Maven repository on the host, look at `docker-compose.yml` in the root folder.

## Development

Follow along via [Github Issues](https://github.com/datacite/content-resolver/issues).

### Note on Patches/Pull Requests

* Fork the project
* Write tests for your new feature or a test that reproduces a bug
* Implement your feature or make a bug fix
* Do not mess with Rakefile, version or history
* Commit, push and make a pull request. Bonus points for topical branches.

## License
**Content Resolver** is released under the [Apache 2 License](https://github.com/datacite/content-resolver/blob/master/LICENSE).
