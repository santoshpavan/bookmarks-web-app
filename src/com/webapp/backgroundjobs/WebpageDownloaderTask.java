package com.webapp.backgroundjobs;

import com.webapp.dao.BookmarkDao;
import com.webapp.entities.WebLink;
import com.webapp.util.HttpConnect;
import com.webapp.util.IOUtil;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WebpageDownloaderTask implements Runnable{
    private static BookmarkDao bookmarkDao = new BookmarkDao();
    private static final long TIME_FRAME = 3000000000L; //3 seconds
    private boolean downloadAll = false;

    // number of threads in the thread pool
    ExecutorService downloadExecutor = Executors.newFixedThreadPool(5);

    private static class Downloader<T extends WebLink> implements Callable<T> {
        private T weblink;
        public Downloader (T weblink) {
            this.weblink = weblink;
        }
        public T call() {
            try {
                if (!weblink.getUrl().endsWith(".pdf")) {
                    // initially setting the status to FAILED. if successful then it can be a success state
                    weblink.setDownloadStatus(WebLink.DownloadStatus.FAILED);
                    String htmlPage = HttpConnect.download(weblink.getUrl());
                    weblink.setHtmlPage(htmlPage);
                } else { // it is a PDF hence not eligible
                    weblink.setDownloadStatus(WebLink.DownloadStatus.NOT_ELIGIBLE);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return weblink;
        }
    }

    public WebpageDownloaderTask(boolean downloadAll) {
        this.downloadAll = downloadAll;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // get weblinks
            List<WebLink> webLinkList = getWeblinks();

            // download concurrently
            if (webLinkList.size() > 0) {
                download(webLinkList);
            } else {
                System.out.println("No new weblinks to download!");
            }

            // wait
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        downloadExecutor.shutdown();
    }

    private void download(List<WebLink> webLinkList) {
        List<Downloader<WebLink>> tasks = getTasks(webLinkList);
        List<Future<WebLink>> futures = new ArrayList<>();

        try {
            futures = downloadExecutor.invokeAll(tasks, TIME_FRAME, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Future<WebLink> future: futures) {
            try {
                if (!future.isCancelled()) {
                    WebLink webLink = future.get();
                    String webPage = webLink.getHtmlPage();
                    if (webPage != null) {
                        IOUtil.write(webPage, webLink.getId());
                        webLink.setDownloadStatus(WebLink.DownloadStatus.SUCCESS);
                        System.out.println("Download Success: " + webLink.getUrl());
                    } else {
                        System.out.println("Webpage not download: " + webLink.getUrl());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Downloader<WebLink>> getTasks(List<WebLink> webLinkList) {
        List<Downloader<WebLink>> tasks = new ArrayList<>();

        for (WebLink webLink: webLinkList) {
            tasks.add(new Downloader<WebLink>(webLink));
        }

        return tasks;
    }

    private List<WebLink> getWeblinks() {
        List<WebLink> webLinkList = null;

        if (downloadAll) { //already existing ones to be downloaded
            webLinkList = bookmarkDao.getAllWebLinks();
            downloadAll = false; //should not download anymore
        } else { //any newly added weblinks
            webLinkList = bookmarkDao.getWebLinks(WebLink.DownloadStatus.NOT_ATTEMPTED);
        }

        return webLinkList;
    }
}
