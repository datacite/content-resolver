<!DOCTYPE html>
<head>
<title>DataCite Content Service Beta</title>
<link rel="stylesheet" type="text/css" href="main.css" />
</head>
<body>
	<h1>
		<img src="/dc-logo.gif"
			alt="DataCite logo"> DataCite Content Service <small>Alpha</small>
	</h1>
	<h2>What is this service?</h2>
	<p>
		This service exposes metadata stored in the <a
			href="http://mds.datacite.org">DataCite Metadata Store (MDS)</a>
		using multiple formats. In the future we plan to add serving content
		(data and custom metadata) direcly from data centres who participate
		in <a href="http://www.datacite.org">DataCite</a>.
	</p>
	<p>Currently we support the following formats:</p>
	<table class="gridtable">
		<tr>
			<th>Format</th>
			<th>Mime type</th>
		</tr>
		<tr>
			<td><a href="http://schema.datacite.org">DataCite XML</a>
			</td>
			<td><code>application/x-datacite+xml</code>
			</td>
		</tr>
		<tr>
			<td>DataCite text citation i.e. Creator (PublicationYear):
				Title. Publisher. Identifier</td>
			<td><code>application/x-datacite+text</code>
			</td>
		</tr>
		<tr>
			<td><a href="http://www.w3.org/TR/rdf-primer/#rdfxml">RDF/XML</a>
			</td>
			<td><code>application/rdf+xml</code>
			</td>
		</tr>
		<tr>
			<td><a href="http://www.w3.org/TeamSubmission/turtle">RDF
					Turtle</a>
			</td>
			<td><code>text/turtle</code>
			</td>
		</tr>
		<tr>
			<td><a href="http://en.wikipedia.org/wiki/BibTeX">BibTex</a>
			</td>
			<td><code>application/x-bibtex</code>
			</td>
		</tr>
		<tr>
			<td><a href="http://en.wikipedia.org/wiki/RIS_(file_format)">RIS</a>
			</td>
			<td><code>application/x-research-info-systems</code>
			</td>
		</tr>
		<tr>
			<td><a href="http://en.wikipedia.org/wiki/HTML">HTML</a>
			</td>
			<td><code>text/html</code>
			</td>
		</tr>
	</table>
	<p>
		There are two ways of using this service: <a
			href="http://en.wikipedia.org/wiki/Content_negotiation">HTTP
			content negotiation</a> or <a
			href="http://www.w3.org/TR/html4/struct/links.html">HTML links</a>.
	</p>
	<h3>Content negotiation</h3>
	<p>
		In this method you will <em>not</em> access this service directly.
		Instead, you will make a DOI resolution via <a
			href="http://dx.doi.org">dx.doi.org</a> using an HTTP client (not
		your regular web browser!) which allows you to specify <a
			href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">HTTP
			Accept header</a>. For example we can use <a href="http://curl.haxx.se">curl</a>
		and request citation text format for 10.5524/100005:
	</p>
	<p>
		<code>curl -L -H "Accept: application/x-datacite+text"
			"http://dx.doi.org/10.5524/100005"</code>
	</p>
	<p>the result will be:</p>
	<p>
		<code>Li, j; Zhang, G; Lambert, D; Wang, J (2011): Genomic data
			from Emperor penguin. GigaScience. http://dx.doi.org/10.5524/100005</code>
	</p>
	<p>Please note that this method relies on DOI resolver being aware
		of this service thus not all DataCite prefixes can be used at the
		moment. If you find out that the prefix of interest is not enabled
		plase let us know.</p>
	<p>
		This part of our implementation is inspired by similar service
		provided by <a
			href="http://www.crossref.org/CrossTech/2011/04/content_negotiation_for_crossr.html">CrossRef</a>
		and the important point is: for standards like RDF which are not
		specific for any <a
			href="http://www.doi.org/registration_agencies.html">IDF
			Registration Agency</a> you resolve DOI with Accept header to get the
		metadata the same way you resolve DOI to get to the landing page i.e.
		without need to know where the DOI was registered in the first place.
	</p>
	<h3>HTML links</h3>

	<p>This method can be used with a regular web browser. In order to
		get a specific format please construct URL following this pattern:
	<p>

		<code>http://data.datacite.org/MIME_TYPE/DOI</code>
	<p>for example, a counterpart for the above citation can be
		obtained by linking to:</p>
	<code>
		<a
			href="http://data.datacite.org/application/x-datacite+text/10.5524/100005">http://data.datacite.org/application/x-datacite+text/10.5524/100005</a>
	</code>
  <p><em>Shortcut</em>: if you omit mime type in the link you will get by default HTML representation:</p>
  <code>
		<a
			href="http://data.datacite.org/10.5524/100005">http://data.datacite.org/10.5524/100005</a>
	</code>
	<h2>Help</h2>
	<p>
		If you have questions please ask at the <a
			href="http://groups.google.com/group/datacite-developers">Developers
			Google Group</a> or contact
		<script>
			var m_ = "mailto:";
			var a_ = "@";
			var d_ = ".";
			var t_ = 'tech' + a_ + 'datacite' + d_ + 'org';
			document.write('<a href="' + m_ + t_ +'">' + t_ + '</a>');
		</script>
		.
	</p>
	<h2>Source code</h2>
	<p>
		This project is hosted on <a href="https://github.com/datacite/conneg">GitHub</a>.
	</p>
</body>
</html>
