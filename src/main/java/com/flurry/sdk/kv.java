package com.flurry.sdk;

import com.flurry.sdk.kw.a;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class kv {
    public static final Integer a = Integer.valueOf(50);
    private static final String d = kv.class.getSimpleName();
    String b;
    LinkedHashMap<String, List<String>> c;

    public kv(String str) {
        a(str);
    }

    void a(String str) {
        this.b = str + "Main";
        e(this.b);
    }

    private void e(String str) {
        this.c = new LinkedHashMap();
        List<String> arrayList = new ArrayList();
        if (j(str)) {
            Collection k = k(str);
            if (k != null && k.size() > 0) {
                arrayList.addAll(k);
                for (String f : arrayList) {
                    f(f);
                }
            }
            i(str);
        } else {
            List<kw> list = (List) new jz(js.a().c().getFileStreamPath(g(this.b)), str, 1, new le<List<kw>>(this) {
                final /* synthetic */ kv a;

                {
                    this.a = r1;
                }

                public lb<List<kw>> a(int i) {
                    return new la(new a());
                }
            }).a();
            if (list == null) {
                kg.c(d, "New main file also not found. returning..");
                return;
            }
            for (kw a : list) {
                arrayList.add(a.a());
            }
        }
        for (String f2 : arrayList) {
            List h = h(f2);
            if (h != null) {
                this.c.put(f2, h);
            }
        }
    }

    private void f(String str) {
        List<String> k = k(str);
        if (k == null) {
            kg.c(d, "No old file to replace");
            return;
        }
        for (String str2 : k) {
            byte[] m = m(str2);
            if (m == null) {
                kg.a(6, d, "File does not exist");
            } else {
                a(str2, m);
                l(str2);
            }
        }
        if (k != null) {
            a(str, k, ".YFlurrySenderIndex.info.");
            i(str);
        }
    }

    private String g(String str) {
        return ".YFlurrySenderIndex.info." + str;
    }

    private synchronized void c() {
        List linkedList = new LinkedList(this.c.keySet());
        b();
        if (!linkedList.isEmpty()) {
            a(this.b, linkedList, this.b);
        }
    }

    public synchronized void a(ku kuVar, String str) {
        Object obj = null;
        synchronized (this) {
            List linkedList;
            kg.a(4, d, "addBlockInfo" + str);
            String a = kuVar.a();
            List list = (List) this.c.get(str);
            if (list == null) {
                kg.a(4, d, "New Data Key");
                linkedList = new LinkedList();
                obj = 1;
            } else {
                linkedList = list;
            }
            linkedList.add(a);
            if (linkedList.size() > a.intValue()) {
                b((String) linkedList.get(0));
                linkedList.remove(0);
            }
            this.c.put(str, linkedList);
            a(str, linkedList, ".YFlurrySenderIndex.info.");
            if (obj != null) {
                c();
            }
        }
    }

    boolean b(String str) {
        return new jz(js.a().c().getFileStreamPath(ku.a(str)), ".yflurrydatasenderblock.", 1, new le<ku>(this) {
            final /* synthetic */ kv a;

            {
                this.a = r1;
            }

            public lb<ku> a(int i) {
                return new ku.a();
            }
        }).b();
    }

    public boolean a(String str, String str2) {
        List list = (List) this.c.get(str2);
        boolean z = false;
        if (list != null) {
            b(str);
            z = list.remove(str);
        }
        if (list == null || list.isEmpty()) {
            d(str2);
        } else {
            this.c.put(str2, list);
            a(str2, list, ".YFlurrySenderIndex.info.");
        }
        return z;
    }

    public List<String> a() {
        return new ArrayList(this.c.keySet());
    }

    public List<String> c(String str) {
        return (List) this.c.get(str);
    }

    public synchronized boolean d(String str) {
        boolean b;
        lt.b();
        jz jzVar = new jz(js.a().c().getFileStreamPath(g(str)), ".YFlurrySenderIndex.info.", 1, new le<List<kw>>(this) {
            final /* synthetic */ kv a;

            {
                this.a = r1;
            }

            public lb<List<kw>> a(int i) {
                return new la(new a());
            }
        });
        List<String> c = c(str);
        if (c != null) {
            kg.a(4, d, "discardOutdatedBlocksForDataKey: notSentBlocks = " + c.size());
            for (String str2 : c) {
                b(str2);
                kg.a(4, d, "discardOutdatedBlocksForDataKey: removed block = " + str2);
            }
        }
        this.c.remove(str);
        b = jzVar.b();
        c();
        return b;
    }

    void b() {
        new jz(js.a().c().getFileStreamPath(g(this.b)), ".YFlurrySenderIndex.info.", 1, new le<List<kw>>(this) {
            final /* synthetic */ kv a;

            {
                this.a = r1;
            }

            public lb<List<kw>> a(int i) {
                return new la(new a());
            }
        }).b();
    }

    private synchronized List<String> h(String str) {
        List<String> arrayList;
        lt.b();
        kg.a(5, d, "Reading Index File for " + str + " file name:" + js.a().c().getFileStreamPath(g(str)));
        List<kw> list = (List) new jz(js.a().c().getFileStreamPath(g(str)), ".YFlurrySenderIndex.info.", 1, new le<List<kw>>(this) {
            final /* synthetic */ kv a;

            {
                this.a = r1;
            }

            public lb<List<kw>> a(int i) {
                return new la(new a());
            }
        }).a();
        arrayList = new ArrayList();
        for (kw a : list) {
            arrayList.add(a.a());
        }
        return arrayList;
    }

    private synchronized void a(String str, byte[] bArr) {
        lt.b();
        kg.a(5, d, "Saving Block File for " + str + " file name:" + js.a().c().getFileStreamPath(ku.a(str)));
        new jz(js.a().c().getFileStreamPath(ku.a(str)), ".yflurrydatasenderblock.", 1, new le<ku>(this) {
            final /* synthetic */ kv a;

            {
                this.a = r1;
            }

            public lb<ku> a(int i) {
                return new ku.a();
            }
        }).a(new ku(bArr));
    }

    private void i(String str) {
        lt.b();
        kg.a(5, d, "Deleting Index File for " + str + " file name:" + js.a().c().getFileStreamPath(".FlurrySenderIndex.info." + str));
        File fileStreamPath = js.a().c().getFileStreamPath(".FlurrySenderIndex.info." + str);
        if (fileStreamPath.exists()) {
            kg.a(5, d, "Found file for " + str + ". Deleted - " + fileStreamPath.delete());
        }
    }

    private synchronized void a(String str, List<String> list, String str2) {
        lt.b();
        kg.a(5, d, "Saving Index File for " + str + " file name:" + js.a().c().getFileStreamPath(g(str)));
        jz jzVar = new jz(js.a().c().getFileStreamPath(g(str)), str2, 1, new le<List<kw>>(this) {
            final /* synthetic */ kv a;

            {
                this.a = r1;
            }

            public lb<List<kw>> a(int i) {
                return new la(new a());
            }
        });
        List arrayList = new ArrayList();
        for (String kwVar : list) {
            arrayList.add(new kw(kwVar));
        }
        jzVar.a(arrayList);
    }

    private synchronized boolean j(String str) {
        File fileStreamPath;
        fileStreamPath = js.a().c().getFileStreamPath(".FlurrySenderIndex.info." + str);
        kg.a(5, d, "isOldIndexFilePresent: for " + str + fileStreamPath.exists());
        return fileStreamPath.exists();
    }

    private synchronized List<String> k(String str) {
        Closeable dataInputStream;
        Throwable th;
        Throwable th2;
        Throwable th3;
        List<String> list = null;
        synchronized (this) {
            lt.b();
            kg.a(5, d, "Reading Index File for " + str + " file name:" + js.a().c().getFileStreamPath(".FlurrySenderIndex.info." + str));
            File fileStreamPath = js.a().c().getFileStreamPath(".FlurrySenderIndex.info." + str);
            List<String> arrayList;
            if (fileStreamPath.exists()) {
                kg.a(5, d, "Reading Index File for " + str + " Found file.");
                try {
                    dataInputStream = new DataInputStream(new FileInputStream(fileStreamPath));
                    try {
                        int readUnsignedShort = dataInputStream.readUnsignedShort();
                        if (readUnsignedShort == 0) {
                            lt.a(dataInputStream);
                        } else {
                            arrayList = new ArrayList(readUnsignedShort);
                            int i = 0;
                            while (i < readUnsignedShort) {
                                try {
                                    int readUnsignedShort2 = dataInputStream.readUnsignedShort();
                                    kg.a(4, d, "read iter " + i + " dataLength = " + readUnsignedShort2);
                                    byte[] bArr = new byte[readUnsignedShort2];
                                    dataInputStream.readFully(bArr);
                                    arrayList.add(new String(bArr));
                                    i++;
                                } catch (Throwable th4) {
                                    th = th4;
                                }
                            }
                            if (dataInputStream.readUnsignedShort() == 0) {
                            }
                            lt.a(dataInputStream);
                            list = arrayList;
                        }
                    } catch (Throwable th32) {
                        th2 = th32;
                        arrayList = null;
                        th = th2;
                        try {
                            kg.a(6, d, "Error when loading persistent file", th);
                            lt.a(dataInputStream);
                            list = arrayList;
                            return list;
                        } catch (Throwable th5) {
                            th32 = th5;
                            lt.a(dataInputStream);
                            throw th32;
                        }
                    }
                } catch (Throwable th6) {
                    th32 = th6;
                    dataInputStream = null;
                    lt.a(dataInputStream);
                    throw th32;
                }
            }
            kg.a(5, d, "Agent cache file doesn't exist.");
            arrayList = null;
            list = arrayList;
        }
        return list;
    }

    private void l(String str) {
        lt.b();
        kg.a(5, d, "Deleting  block File for " + str + " file name:" + js.a().c().getFileStreamPath(".flurrydatasenderblock." + str));
        File fileStreamPath = js.a().c().getFileStreamPath(".flurrydatasenderblock." + str);
        if (fileStreamPath.exists()) {
            kg.a(5, d, "Found file for " + str + ". Deleted - " + fileStreamPath.delete());
        }
    }

    private byte[] m(String str) {
        Closeable dataInputStream;
        Throwable th;
        Throwable th2;
        byte[] bArr = null;
        lt.b();
        kg.a(5, d, "Reading block File for " + str + " file name:" + js.a().c().getFileStreamPath(".flurrydatasenderblock." + str));
        File fileStreamPath = js.a().c().getFileStreamPath(".flurrydatasenderblock." + str);
        if (fileStreamPath.exists()) {
            kg.a(5, d, "Reading Index File for " + str + " Found file.");
            try {
                dataInputStream = new DataInputStream(new FileInputStream(fileStreamPath));
                try {
                    int readUnsignedShort = dataInputStream.readUnsignedShort();
                    if (readUnsignedShort == 0) {
                        lt.a(dataInputStream);
                    } else {
                        bArr = new byte[readUnsignedShort];
                        dataInputStream.readFully(bArr);
                        if (dataInputStream.readUnsignedShort() == 0) {
                            lt.a(dataInputStream);
                        } else {
                            lt.a(dataInputStream);
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    try {
                        kg.a(6, d, "Error when loading persistent file", th);
                        lt.a(dataInputStream);
                        return bArr;
                    } catch (Throwable th4) {
                        th2 = th4;
                        lt.a(dataInputStream);
                        throw th2;
                    }
                }
            } catch (Throwable th5) {
                dataInputStream = null;
                th2 = th5;
                lt.a(dataInputStream);
                throw th2;
            }
        }
        kg.a(4, d, "Agent cache file doesn't exist.");
        return bArr;
    }
}
