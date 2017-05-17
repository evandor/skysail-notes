package io.skysail.testsupport

import org.restlet.data.MediaType
import org.slf4j.LoggerFactory
import io.skysail.testsupport.authentication.AuthenticationStrategy2
import org.restlet.resource.ClientResource
import org.restlet.data.ChallengeResponse
import org.restlet.representation.Representation
import org.restlet.data.Method

object ScalaApplicationClient {
  val TESTTAG = " > TEST: ";
}

class ScalaApplicationClient(val baseUrl: String, appName: String, mediaType: MediaType) {

  private val log = LoggerFactory.getLogger(this.getClass())

  //    @Getter
  //    private String baseUrl;
  var credentials = ""
  var url = ""
  var cr: ClientResource = _
  //    @Getter
  //    private Representation currentRepresentation;
  //    private MediaType mediaType = MediaType.TEXT_HTML;
  //    private String appName;
  //
  //    @Getter
  //    private Series<Header> currentHeader;

  var challengeResponse: ChallengeResponse = _

  def setUrl(url: String): ScalaApplicationClient = {
    log.info(s"$ScalaApplicationClient.TESTTAG setting browser client url to '$url'");
    this.url = url;
    return this
  }

  def get(): Representation = {
    val currentUrl = baseUrl + url;
    log.info(s"$ScalaApplicationClient.TESTTAG issuing GET on '$currentUrl', providing credentials $credentials")
    cr = new ClientResource(currentUrl);
    //cr.setFollowingRedirects(false);
    if (credentials != null && credentials.trim().size > 0) {
      cr.getCookies().add("Credentials", credentials);
    }
    cr.setChallengeResponse(challengeResponse);
    return cr.get(mediaType);
  }

  def gotoRoot() = {
    url = "/";
    get();
    this
  }

  def gotoAppRoot(): ScalaApplicationClient = {
    gotoRoot().followLinkTitle(appName);
    this
  }
  //
  //    public ApplicationClient2 gotoUrl(String relUrl) {
  //        url = relUrl;
  //        currentRepresentation = get();
  //        return this;
  //    }
  //
  //
  def post(entity: AnyRef): Representation = {
    log.info(s"$ScalaApplicationClient.TESTTAG issuing POST on '$url', providing credentials $credentials");
    //url = if (url.contains("?")) url + "&" else url + "?") + "xxx";//SkysailServerResource.NO_REDIRECTS ;
    cr = new ClientResource(url);
    cr.setFollowingRedirects(false);
    cr.getCookies().add("Credentials", credentials);
    cr.setChallengeResponse(challengeResponse);
    return cr.post(entity, mediaType);
  }

  //    public Response getResponse() {
  //        return cr.getResponse();
  //    }
  //
  def loginAs(authenticationStrategy: AuthenticationStrategy2, username: String, password: String): ScalaApplicationClient = {
    cr = authenticationStrategy.login(this, username, password);
    challengeResponse = cr.getChallengeResponse();
    credentials = cr.getCookies().getFirstValue("Credentials");
    return this;
  }

  def followLinkTitle(linkTitle: String) = {
    follow(new ScalaLinkTitlePredicate(linkTitle, cr.getResponse().getHeaders()));
  }

  //    public ApplicationClient2 followLinkTitleAndRefId(String linkTitle, String refId) {
  //    	LinkModel example = new LinkModel(refId, "", null, null, null);//new LinkModel.Builder("").title(linkTitle).refId(refId).build();
  //        return follow(new LinkByExamplePredicate(example, cr.getResponse().getHeaders()));
  //    }
  //
  //    public ApplicationClient2 followLinkRelation(LinkRelation linkRelation) {
  //        return follow(new LinkRelationPredicate(linkRelation, cr.getResponse().getHeaders()));
  //    }
  //
  //    public ApplicationClient2 followLink(Method method) {
  //        return followLink(method, null);
  //    }
  //
  //    public ApplicationClient2 followLink(Method method, String entity) {
  //        return follow(new LinkMethodPredicate(method, cr.getResponse().getHeaders()), method, entity);
  //    }
  //
      private def follow(predicate: ScalaLinkPredicate , method: Method , entity:String ) = {
          val currentHeader = cr.getResponse().getHeaders();
          val linkheader = currentHeader.getFirstValue("Link");
          if (linkheader == null) {
              throw new IllegalStateException("no link header found");
          }
//          List<LinkModel> links = Arrays.stream(linkheader.split(",")).map(l -> LinkModel.fromLinkheader(l)).collect(Collectors.toList());
//          val theLink = getTheOnlyLink(predicate, links);
//  
//          var isAbsolute = false;
//          try {
//              val url2 = new URI(theLink.getUri());
//              isAbsolute = url2.isAbsolute();
//          } catch (URISyntaxException e) {
//              // TODO Auto-generated catch block
//              e.printStackTrace();
//          }
  
//          url =  isAbsolute ? theLink.getUri() : baseUrl + theLink.getUri();
//          cr = new ClientResource(url);
//          cr.getCookies().add("Credentials", credentials);
//          cr.setChallengeResponse(challengeResponse);
//  
//          if (method != null) {
//          	// TODO
//  //            if (!(theLink.getVerbs().contains(method))) {
//  //                throw new IllegalStateException("method " + method + " not eligible for link " + theLink);
//  //            }
//              if (Method.DELETE.equals(method)) {
//                  log.info("{}issuing DELETE on '{}', providing credentials {}", TESTTAG, url, credentials);
//                  currentRepresentation = cr.delete(mediaType);
//              } else if (Method.POST.equals(method)) {
//                  log.info("{}issuing POST on '{}' with entity '{}', providing credentials {}", TESTTAG, url, entity, credentials);
//                  currentRepresentation = cr.post(entity, mediaType);
//              } else if (Method.PUT.equals(method)) {
//                  log.info("{}issuing PUT on '{}' with entity '{}', providing credentials {}", TESTTAG, url, entity, credentials);
//                  currentRepresentation = cr.put(entity, mediaType);
//              } else {
//                  throw new UnsupportedOperationException();
//              }
//          } else {
//              currentRepresentation = cr.get(mediaType);
//              //url = currentRepresentation.getLocationRef().toUri().toString();
//          }
          this;
      }
  
  private def follow(predicate: ScalaLinkPredicate): ScalaApplicationClient = follow(predicate, null, null)
  //
  //    private LinkModel getTheOnlyLink(LinkPredicate predicate, List<LinkModel> links) {
  //        List<LinkModel> filteredLinks = links.stream().filter(predicate).collect(Collectors.toList());
  //        if (filteredLinks.size() == 0 && predicate instanceof LinkTitlePredicate && !(predicate instanceof LinkSubTitlePredicate)) {
  //            log.info("didn't find exact link, trying substring");
  //            return getTheOnlyLink(new LinkSubTitlePredicate((LinkTitlePredicate)predicate), links);
  //        }
  //        if (filteredLinks.size() == 0) {
  //            throw new IllegalStateException("could not find link for predicate " + predicate);
  //        }
  //        if (filteredLinks.size() > 1) {
  //            throw new IllegalStateException("too many candidates found for predicate " + predicate);
  //        }
  //        LinkModel theLink = filteredLinks.get(0);
  //        return theLink;
  //    }
  //
  def getLocation() = cr.getLocationRef()

  //    public void setUrlFromCurrentRepresentation() {
  //        url = currentRepresentation.getLocationRef().toUri().toString();
  //    }

}